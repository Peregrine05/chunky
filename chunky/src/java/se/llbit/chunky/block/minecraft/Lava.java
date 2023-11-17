/*
 * Copyright (c) 2023 Chunky contributors
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

package se.llbit.chunky.block.minecraft;

import se.llbit.chunky.block.MinecraftBlockTranslucent;
import se.llbit.chunky.renderer.scene.Scene;
import se.llbit.chunky.resources.Texture;
import se.llbit.math.*;
import se.llbit.util.VectorUtil;

import static se.llbit.chunky.block.minecraft.Water.CORNER_0;
import static se.llbit.chunky.block.minecraft.Water.CORNER_1;
import static se.llbit.chunky.block.minecraft.Water.CORNER_2;
import static se.llbit.chunky.block.minecraft.Water.CORNER_3;
import static se.llbit.chunky.block.minecraft.Water.FULL_BLOCK;

public class Lava extends MinecraftBlockTranslucent {
  private static final AABB fullBlock = new AABB(0, 1, 0, 1, 0, 1);

  private static final Quad bottom =
      new Quad(new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 0, 1),
          new Vector4(0, 1, 0, 1));

  public final int level;
  public final int data;

  public Lava(int level, int data) {
    super("lava", Texture.lava);
    this.level = level % 8;
    this.data = data;
    solid = false;
    localIntersect = true;
    emittance = 1.0f;
  }

  public Lava(int level) {
    this(level, 1 << FULL_BLOCK);
  }

  public boolean isFullBlock() {
    return (this.data & (1 << FULL_BLOCK)) != 0;
  }

  @Override public boolean intersect(Ray2 ray, IntersectionRecord intersectionRecord, Scene scene) {
    if (isFullBlock()) {
      if (fullBlock.intersect(ray, intersectionRecord)) {
        texture.getColor(intersectionRecord);
        return true;
      }
      return false;
    }

    boolean hit = false;
    IntersectionRecord intersectionTest = new IntersectionRecord();
    if (bottom.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, bottom.n));
      hit = true;
    }

    int c0 = (0xF & (data >> CORNER_0)) % 8;
    int c1 = (0xF & (data >> CORNER_1)) % 8;
    int c2 = (0xF & (data >> CORNER_2)) % 8;
    int c3 = (0xF & (data >> CORNER_3)) % 8;
    Triangle triangle = Water.t012[c0][c1][c2];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      hit = true;
    }
    triangle = Water.t230[c2][c3][c0];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      intersectionTest.uv.x = 1 - intersectionTest.uv.x;
      intersectionTest.uv.y = 1 - intersectionTest.uv.y;
      hit = true;
    }
    triangle = Water.westt[c0][c3];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      double z = intersectionTest.distance * ray.d.z + ray.o.z;
      y -= QuickMath.floor(y);
      z -= QuickMath.floor(z);
      intersectionTest.uv.x = z;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.westb[c0];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      double z = intersectionTest.distance * ray.d.z + ray.o.z;
      y -= QuickMath.floor(y);
      z -= QuickMath.floor(z);
      intersectionTest.uv.x = z;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.eastt[c1][c2];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      double z = intersectionTest.distance * ray.d.z + ray.o.z;
      y -= QuickMath.floor(y);
      z -= QuickMath.floor(z);
      intersectionTest.uv.x = z;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.eastb[c1];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      double z = intersectionTest.distance * ray.d.z + ray.o.z;
      y -= QuickMath.floor(y);
      z -= QuickMath.floor(z);
      intersectionTest.uv.x = z;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.southt[c0][c1];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double x = intersectionTest.distance * ray.d.x + ray.o.x;
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      x -= QuickMath.floor(x);
      y -= QuickMath.floor(y);
      intersectionTest.uv.x = x;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.southb[c1];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double x = intersectionTest.distance * ray.d.x + ray.o.x;
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      x -= QuickMath.floor(x);
      y -= QuickMath.floor(y);
      intersectionTest.uv.x = x;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.northt[c2][c3];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double x = intersectionTest.distance * ray.d.x + ray.o.x;
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      x -= QuickMath.floor(x);
      y -= QuickMath.floor(y);
      intersectionTest.uv.x = 1 - x;
      intersectionTest.uv.y = y;
      hit = true;
    }
    triangle = Water.northb[c2];
    if (triangle.intersect(ray, intersectionTest)) {
      intersectionRecord.setNormal(VectorUtil.orientNormal(ray.d, triangle.n));
      double x = intersectionTest.distance * ray.d.x + ray.o.x;
      double y = intersectionTest.distance * ray.d.y + ray.o.y;
      x -= QuickMath.floor(x);
      y -= QuickMath.floor(y);
      intersectionTest.uv.x = 1 - x;
      intersectionTest.uv.y = y;
      hit = true;
    }
    if (hit) {
      texture.getColor(intersectionTest);
      intersectionRecord.color.set(intersectionTest.color);
      intersectionRecord.color.w = 1;
      intersectionRecord.distance += intersectionTest.distance;
      return true;
    }
    return false;
  }

  @Override public String description() {
    return "level=" + level;
  }
}
