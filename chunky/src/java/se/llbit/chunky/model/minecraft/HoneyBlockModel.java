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

package se.llbit.chunky.model.minecraft;

import se.llbit.chunky.model.QuadModel;
import se.llbit.chunky.resources.Texture;
import se.llbit.math.*;

public class HoneyBlockModel extends QuadModel {
    private static final Quad[] quads = {
      new Quad(
        new Vector3(16 / 16.0, 16 / 16.0, 0 / 16.0),
        new Vector3(0 / 16.0, 16 / 16.0, 0 / 16.0),
        new Vector3(16 / 16.0, 16 / 16.0, 16 / 16.0),
        new Vector4(0, 1, 0, 1),
        true
      ),
      new Quad(
        new Vector3(0 / 16.0, 0 / 16.0, 0 / 16.0),
        new Vector3(16 / 16.0, 0 / 16.0, 0 / 16.0),
        new Vector3(0 / 16.0, 0 / 16.0, 16 / 16.0),
        new Vector4(0, 1, 0, 1),
        true
      ),
      new Quad(
        new Vector3(16 / 16.0, 0 / 16.0, 16 / 16.0),
        new Vector3(16 / 16.0, 0 / 16.0, 0 / 16.0),
        new Vector3(16 / 16.0, 16 / 16.0, 16 / 16.0),
        new Vector4(0, 1, 0, 1),
        true
      ),
      new Quad(
        new Vector3(0 / 16.0, 0 / 16.0, 0 / 16.0),
        new Vector3(0 / 16.0, 0 / 16.0, 16 / 16.0),
        new Vector3(0 / 16.0, 16 / 16.0, 0 / 16.0),
        new Vector4(0, 1, 0, 1),
        true
      ),
      new Quad(
        new Vector3(16 / 16.0, 0 / 16.0, 0 / 16.0),
        new Vector3(0 / 16.0, 0 / 16.0, 0 / 16.0),
        new Vector3(16 / 16.0, 16 / 16.0, 0 / 16.0),
        new Vector4(0, 1, 0, 1),
        true
      ),
      new Quad(
        new Vector3(0 / 16.0, 0 / 16.0, 16 / 16.0),
        new Vector3(16 / 16.0, 0 / 16.0, 16 / 16.0),
        new Vector3(0 / 16.0, 16 / 16.0, 16 / 16.0),
        new Vector4(0, 1, 0, 1),
        true
      ),
      new Quad(
        new Vector3(15 / 16.0, 15 / 16.0, 1 / 16.0),
        new Vector3(1 / 16.0, 15 / 16.0, 1 / 16.0),
        new Vector3(15 / 16.0, 15 / 16.0, 15 / 16.0),
        new Vector4(1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        true
      ),
      new Quad(
        new Vector3(1 / 16.0, 1 / 16.0, 1 / 16.0),
        new Vector3(15 / 16.0, 1 / 16.0, 1 / 16.0),
        new Vector3(1 / 16.0, 1 / 16.0, 15 / 16.0),
        new Vector4(1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        true
      ),
      new Quad(
        new Vector3(15 / 16.0, 1 / 16.0, 15 / 16.0),
        new Vector3(15 / 16.0, 1 / 16.0, 1 / 16.0),
        new Vector3(15 / 16.0, 15 / 16.0, 15 / 16.0),
        new Vector4(1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        true
      ),
      new Quad(
        new Vector3(1 / 16.0, 1 / 16.0, 1 / 16.0),
        new Vector3(1 / 16.0, 1 / 16.0, 15 / 16.0),
        new Vector3(1 / 16.0, 15 / 16.0, 1 / 16.0),
        new Vector4(1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        true
      ),
      new Quad(
        new Vector3(15 / 16.0, 1 / 16.0, 1 / 16.0),
        new Vector3(1 / 16.0, 1 / 16.0, 1 / 16.0),
        new Vector3(15 / 16.0, 15 / 16.0, 1 / 16.0),
        new Vector4(1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        true
      ),
      new Quad(
        new Vector3(1 / 16.0, 1 / 16.0, 15 / 16.0),
        new Vector3(15 / 16.0, 1 / 16.0, 15 / 16.0),
        new Vector3(1 / 16.0, 15 / 16.0, 15 / 16.0),
        new Vector4(1 / 16.0, 15 / 16.0, 1 / 16.0, 15 / 16.0),
        true
      ),
    };

    private static final Texture[] textures = {
            Texture.honeyBlockBottom, Texture.honeyBlockBottom, Texture.honeyBlockBottom,
            Texture.honeyBlockBottom, Texture.honeyBlockBottom, Texture.honeyBlockBottom,

            Texture.honeyBlockBottom, Texture.honeyBlockTop, Texture.honeyBlockSide,
            Texture.honeyBlockSide, Texture.honeyBlockSide, Texture.honeyBlockSide
    };

  public HoneyBlockModel() {
    refractive = true;
  }

  @Override
  public Quad[] getQuads() {
    return quads;
  }

  @Override
  public Texture[] getTextures() {
    return textures;
  }

  @Override
  public boolean isInside(Ray2 ray) {
    return true;
  }
}
