/*
 * Copyright (C) 2015-2022 Philip Helger (www.helger.com)
 * philip[at]helger[dot]com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.helger.phase4.servlet.dump;

import java.io.File;

import javax.annotation.Nonnull;

import com.helger.commons.ValueEnforcer;
import com.helger.phase4.client.IAS4RawResponseConsumer;
import com.helger.phase4.config.AS4Configuration;

/**
 * Example implementation of {@link IAS4RawResponseConsumer} writing to a file.
 * Deprecated version. Use the one in package
 * <code>com.helger.phase4.dump</code>.
 *
 * @author Philip Helger
 * @deprecated In v1.3.0 - use the class in package
 *             <code>com.helger.phase4.dump</code>
 */
@Deprecated
public class AS4RawResponseConsumerWriteToFile extends com.helger.phase4.dump.AS4RawResponseConsumerWriteToFile
{
  /**
   * Callback interface to create a file based on the provided metadata.
   *
   * @author Philip Helger
   * @since 0.10.2
   */
  @FunctionalInterface
  @Deprecated
  public interface IFileProvider extends com.helger.phase4.dump.AS4RawResponseConsumerWriteToFile.IFileProvider
  {
    @Nonnull
    static String getFilename (@Nonnull final String sAS4MessageID)
    {
      return com.helger.phase4.dump.AS4RawResponseConsumerWriteToFile.IFileProvider.getFilename (sAS4MessageID);
    }
  }

  /**
   * Default constructor. Writes the files to the AS4 configured data path +
   * {@link AS4OutgoingDumperFileBased#DEFAULT_BASE_PATH}.
   *
   * @see AS4Configuration#getDumpBasePathFile()
   */
  public AS4RawResponseConsumerWriteToFile ()
  {}

  /**
   * Constructor with a custom file provider.
   *
   * @param aFileProvider
   *        The file provider to be used. May not be <code>null</code>.
   * @since 0.10.2
   */
  public AS4RawResponseConsumerWriteToFile (@Nonnull final IFileProvider aFileProvider)
  {
    super (aFileProvider);
  }

  /**
   * Create a new instance for the provided directory.
   *
   * @param aBaseDirectory
   *        The absolute directory to be used. May not be <code>null</code>.
   * @return The created instance. Never <code>null</code>.
   * @since 0.10.2
   */
  @Nonnull
  public static AS4RawResponseConsumerWriteToFile createForDirectory (@Nonnull final File aBaseDirectory)
  {
    ValueEnforcer.notNull (aBaseDirectory, "BaseDirectory");
    return new AS4RawResponseConsumerWriteToFile (sMessageID -> new File (aBaseDirectory,
                                                                          com.helger.phase4.dump.AS4RawResponseConsumerWriteToFile.IFileProvider.getFilename (sMessageID)));
  }
}
