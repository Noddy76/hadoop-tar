package org.queeg.hadoop.tar;

import java.io.File;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.beust.jcommander.JCommander;
import com.google.common.io.Files;

public class Tar extends Configured implements Tool {

  @Override
  public int run(String[] args) throws Exception {
    TarArgs tarArgs = new TarArgs();
    new JCommander(tarArgs, args);
    tarArgs.validate();

    if (tarArgs.isExtract()) {
      TarExtractor extractor = new TarExtractor(getConf(), new Path(tarArgs.getArgs().get(0)));
      extractor.extract(Files.asByteSource(new File(tarArgs.getFilename())));
    } else {
      throw new UnsupportedOperationException();
    }
    return 0;
  }

  public static void main(String[] args) throws Exception {
    int status = ToolRunner.run(new Tar(), args);
    if (status != 0) {
      System.exit(status);
    }
  }
}
