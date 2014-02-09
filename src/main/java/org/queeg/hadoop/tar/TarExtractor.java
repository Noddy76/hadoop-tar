/**
 *    Copyright 2014 James Grant
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.queeg.hadoop.tar;

import java.io.IOException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.BoundedInputStream;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.io.ByteSink;
import com.google.common.io.ByteSource;
import com.google.common.io.ByteStreams;

public class TarExtractor {
  private Configuration conf;
  private Path destination;
  private FileSystem fs;

  public TarExtractor(Configuration conf, Path destination) throws IOException {
    this.conf = conf;
    this.destination = destination;
    fs = destination.getFileSystem(conf);
  }

  public void extract(ByteSource source) throws IOException {
    TarArchiveInputStream archiveInputStream = new TarArchiveInputStream(source.openStream());

    TarArchiveEntry entry;
    while ((entry = archiveInputStream.getNextTarEntry()) != null) {
      if (entry.isFile()) {
        BoundedInputStream entryInputStream = new BoundedInputStream(archiveInputStream, entry.getSize());
        ByteSink sink = new PathByteSink(conf, new Path(destination, entry.getName()));
        sink.writeFrom(entryInputStream);
      } else if (entry.isDirectory()) {
        ByteStreams.skipFully(archiveInputStream, entry.getSize());
        fs.mkdirs(new Path(destination, entry.getName()));
      }
    }

    archiveInputStream.close();
  }
}
