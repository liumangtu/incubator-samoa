package com.yahoo.labs.samoa.utils;

/*
 * #%L
 * SAMOA
 * %%
 * Copyright (C) 2014 - 2015 Apache Software Foundation
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * Serialize and deserialize objects with Java serialization
 * 
 * @author Anh Thu Vu
 */
public class SerializableSerializer extends Serializer<Object> {
  @Override
  public void write(Kryo kryo, Output output, Object object) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream oos = new ObjectOutputStream(bos);
      oos.writeObject(object);
      oos.flush();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    byte[] ser = bos.toByteArray();
    output.writeInt(ser.length);
    output.writeBytes(ser);
  }

  @SuppressWarnings("rawtypes")
  @Override
  public Object read(Kryo kryo, Input input, Class c) {
    int len = input.readInt();
    byte[] ser = new byte[len];
    input.readBytes(ser);
    ByteArrayInputStream bis = new ByteArrayInputStream(ser);
    try {
      ObjectInputStream ois = new ObjectInputStream(bis);
      return ois.readObject();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
