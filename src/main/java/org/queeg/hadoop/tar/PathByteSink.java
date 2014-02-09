package org.queeg.hadoop.tar;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.google.common.io.ByteSink;

public class PathByteSink extends ByteSink {

  private FileSystem fs;
  private Path path;

  public PathByteSink(Configuration conf, Path path) throws IOException {
    this.path = path;
    fs = path.getFileSystem(conf);
  }

  @Override
  public OutputStream openStream() throws IOException {
    return fs.create(path, true);
  }
}
