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
