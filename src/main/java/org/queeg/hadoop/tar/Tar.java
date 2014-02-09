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
