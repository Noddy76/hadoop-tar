package org.queeg.hadoop.tar;

import static com.google.common.base.Preconditions.checkState;

import java.util.List;

import com.beust.jcommander.Parameter;

public class TarArgs {

  @Parameter(names = { "-c", "--create" }, description = "Create a new archive")
  private boolean create = false;

  @Parameter(names = { "-x", "--extract", "--get" }, description = "Extract files from the archive")
  private boolean extract = false;

  @Parameter(names = { "-f", "--file" }, description = "Archive filename", required = true)
  private String filename;

  @Parameter(names = { "-z", "--gzip" }, description = "Gzip compress/decompesss the archive")
  private boolean gzip = false;

  @Parameter(names = { "-j", "--bzip2" }, description = "Bzip2 compress/decompress the archive")
  private boolean bzip = false;

  @Parameter
  private List<String> args;

  public void validate() throws IllegalStateException {
    checkState(isCreate() || isExtract(), "One of create or extract must be specified");
    if (isCreate()) {
      checkState(!isExtract(), "Only one of create or extract may be specified");
    }

    if (isGzip()) {
      checkState(!isBzip(), "Only one compression scheme my be specified");
    }
  }

  public boolean isCreate() {
    return create;
  }

  public boolean isExtract() {
    return extract;
  }

  public String getFilename() {
    return filename;
  }

  public boolean isGzip() {
    return gzip;
  }

  public boolean isBzip() {
    return bzip;
  }

  public List<String> getArgs() {
    return args;
  }
}
