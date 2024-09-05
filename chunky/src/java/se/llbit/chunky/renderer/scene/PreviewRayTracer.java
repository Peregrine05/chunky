/* Copyright (c) 2013-2022 Jesper Öqvist <jesper@llbit.se>
 * Copyright (c) 2013-2022 Chunky contributors
 *
 * This file is part of Chunky.
 *
 * Chunky is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Chunky is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with Chunky.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.llbit.chunky.renderer.scene;

import se.llbit.chunky.block.Block;
import se.llbit.chunky.block.minecraft.Air;
import se.llbit.chunky.block.MinecraftBlock;
import se.llbit.chunky.block.minecraft.Water;
import se.llbit.chunky.renderer.WorkerState;
import se.llbit.chunky.world.Material;
import se.llbit.math.Constants;
import se.llbit.math.IntersectionRecord;
import se.llbit.math.Ray;
import se.llbit.math.Ray2;
import se.llbit.math.Vector3;
import se.llbit.math.Vector4;

/**
 * @author Jesper Öqvist <jesper@llbit.se>
 */
public class PreviewRayTracer implements RayTracer {

  /**
   * Do a quick preview ray tracing for the current ray.
   */
  @Override public void trace(Scene scene, WorkerState state) {
    Ray2 ray = state.ray;
    ray.flags |= Ray2.SPECULAR | Ray2.INDIRECT;
    ray.setCurrentMedium(scene.getWorldMaterial(ray));

    IntersectionRecord intersectionRecord = state.intersectionRecord;
    Vector3 throughput = state.throughput;

    for (int i = scene.rayDepth; i > 0; i--) {
      intersectionRecord.reset();
      if (scene.intersect(ray, intersectionRecord, null)) {
        ray.o.scaleAdd(intersectionRecord.distance, ray.d);
        ray.getCurrentMedium().absorption(throughput, intersectionRecord.distance);
        ray.clearReflectionFlags();
        if (intersectionRecord.material.scatter(ray, intersectionRecord, state.emittance, state.random)) {
          ray.setCurrentMedium(intersectionRecord.material);
        }
        if ((ray.flags & Ray2.DIFFUSE) != 0) {
          scene.sun.flatShading(intersectionRecord);
          break;
        } else {
          throughput.x *= intersectionRecord.color.x;
          throughput.y *= intersectionRecord.color.y;
          throughput.z *= intersectionRecord.color.z;
        }
      } else if (mapIntersection(scene, ray, intersectionRecord)) {
        break;
      } else {
        scene.sky.intersect(ray, intersectionRecord);
        break;
      }
    }
    state.color.x = throughput.x * intersectionRecord.color.x;
    state.color.y = throughput.y * intersectionRecord.color.y;
    state.color.z = throughput.z * intersectionRecord.color.z;
  }

  /**
   * Calculate sky occlusion.
   * @return occlusion value (1 = occluded, 0 = transparent)
   */
  public static double skyOcclusion(Scene scene, WorkerState state) {
    Ray2 ray = state.ray;
    IntersectionRecord intersectionRecord;
    double occlusion = 1.0;
    while (true) {
      intersectionRecord = new IntersectionRecord();
      if (!scene.intersect(ray, intersectionRecord, state.random)) {
        break;
      } else {
        occlusion *= (1 - intersectionRecord.color.w);
        if (occlusion == 0) {
          return 1; // occlusion can't become > 0 anymore
        }
        ray.o.scaleAdd((intersectionRecord.distance + Constants.OFFSET), ray.d);
      }
    }
    return 1 - occlusion;
  }

//  /**
//   * Find next ray intersection.
//   * @return true if intersected, false if no intersection has been found
//   */
//  public static boolean nextIntersection(Scene scene, Ray ray) {
//    ray.setPrevMaterial(ray.getCurrentMaterial(), ray.getCurrentData());
//    ray.t = Double.POSITIVE_INFINITY;
//    boolean hit = false;
//    if (scene.sky().cloudsEnabled()) {
//      hit = scene.sky().cloudIntersection(scene, ray);
//    }
//    if (scene.isWaterPlaneEnabled()) {
//      hit = waterPlaneIntersection(scene, ray) || hit;
//    }
//    if (scene.intersect(ray)) {
//      // Octree tracer handles updating distance.
//      return true;
//    }
//    if (hit) {
//      ray.distance += ray.t;
//      ray.o.scaleAdd(ray.t, ray.d);
//      scene.updateOpacity(ray);
//      return true;
//    } else {
//      ray.setCurrentMaterial(Air.INSTANCE);
//      return false;
//    }
//  }

//  private static boolean waterPlaneIntersection(Scene scene, Ray ray) {
//    double t = (scene.getEffectiveWaterPlaneHeight() - ray.o.y - scene.origin.y) / ray.d.y;
//    if (scene.getWaterPlaneChunkClip()) {
//      Vector3 pos = new Vector3(ray.o);
//      pos.scaleAdd(t, ray.d);
//      if (scene.isChunkLoaded((int)Math.floor(pos.x), (int)Math.floor(pos.y), (int)Math.floor(pos.z)))
//        return false;
//    }
//    if (ray.d.y < 0) {
//      if (t > 0 && t < ray.t) {
//        ray.t = t;
//        Water.INSTANCE.getColor(ray);
//        ray.setNormal(0, 1, 0);
//        ray.setCurrentMaterial(scene.getPalette().water);
//        return true;
//      }
//    }
//    if (ray.d.y > 0) {
//      if (t > 0 && t < ray.t) {
//        ray.t = t;
//        Water.INSTANCE.getColor(ray);
//        ray.setNormal(0, -1, 0);
//        ray.setCurrentMaterial(Air.INSTANCE);
//        return true;
//      }
//    }
//    return false;
//  }

  // Chunk pattern config
  private static final double chunkPatternLineWidth = 0.5; // in blocks
  private static final double chunkPatternLinePosition = 8 - chunkPatternLineWidth / 2;
  private static final Vector4 chunkPatternFillColor =
    new Vector4(0.8, 0.8, 0.8, 1.0);
  private static final Vector4 chunkPatternLineColor =
    new Vector4(0.25, 0.25, 0.25, 1.0);
  private static final Vector4 chunkPatternFillColorSubmerged =
    new Vector4(0.6, 0.6, 0.8, 1.0);
  private static final Vector4 chunkPatternLineColorSubmerged =
    new Vector4(0.05, 0.05, 0.25, 1.0);
  private static final double chunkPatternInsideOctreeColorFactor = 0.75;

  /**
   * Projects a chunk border pattern onto the bottom plane of the octree (yMin).
   * Changes colors for chunks inside the octree and submerged scenes.
   * Use only in preview mode - the ray should hit the sky in a real render.
   */
  private static boolean mapIntersection(Scene scene, Ray2 ray, IntersectionRecord intersectionRecord) {
    if (ray.d.y < 0) { // ray going below horizon
      double t = (scene.yMin - ray.o.y - scene.origin.y) / ray.d.y;
      if (t > 0 && t < intersectionRecord.distance) {
        Vector3 point = new Vector3(ray.o);
        point.scaleAdd(t + Constants.OFFSET, ray.d);
        // must be submerged if water plane is enabled otherwise ray already had collided with water
        boolean isSubmerged = scene.isWaterPlaneEnabled();
        boolean insideOctree = scene.isInsideOctree(point);
        intersectionRecord.distance = t;
        ray.o.set(point);
        double xm = ((ray.o.x) % 16.0 + 8.0) % 16.0;
        double zm = ((ray.o.z) % 16.0 + 8.0) % 16.0;
        if (
          (xm < chunkPatternLinePosition || xm > chunkPatternLinePosition + chunkPatternLineWidth) &&
            (zm < chunkPatternLinePosition || zm > chunkPatternLinePosition + chunkPatternLineWidth)
        ) { // chunk fill
          if (isSubmerged) {
            intersectionRecord.color.set(chunkPatternFillColorSubmerged);
          } else {
            intersectionRecord.color.set(chunkPatternFillColor);
          }
        } else { // chunk border
          if (isSubmerged) {
            intersectionRecord.color.set(chunkPatternLineColorSubmerged);
          } else {
            intersectionRecord.color.set(chunkPatternLineColor);
          }
        }
        if(insideOctree) {
          intersectionRecord.color.scale(chunkPatternInsideOctreeColorFactor);
        }
        // handle like a solid horizontal plane
        intersectionRecord.material = MinecraftBlock.STONE;
        intersectionRecord.setNormal(0, 1, 0);
        return true;
      }
    }
    return false;
  }
}
