/**
 *  Copyright 2016 Peter Nerg
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package zookeeperjunit;

import static zookeeperjunit.FileUtil.mkdir;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.FileAlreadyExistsException;
import java.util.Properties;

/**
 * Utilities for file management.
 * @author Peter Nerg
 * @since 1.0
 */
final class FileUtil {
    /**
     * Creates the requested directory. <br>
     * If it already exists it will be deleted and then re-created.
     * 
     * @param parent The parent directory
     * @param name The name of the sub-directory
     * @return The created directory
     * @throws IOException If the directory already exists or could not be created
     */
     static File mkdir(File parent, String name) throws IOException {
        File dir = new File(parent, name);

        // delete any existing directory, bail if we fail to delete the directory
        if (dir.exists()) {
            throw new FileAlreadyExistsException("Directory already exists [" + dir.getAbsolutePath() + "]");
        }

        // bail if we fail to create the directory
        if (!dir.mkdirs()) {
            throw new IOException("Failed to create dir [" + dir.getAbsolutePath() + "]");
        }
        
        return dir;
    }
     
     /**
      * Deletes the provided file path. <br>
      * In case the path denotes a directory the method will recursively go through the provided directory and delete all files/directories it finds.
      * 
      * @param path The path to delete
      * @return <code>true</code> only if the path and all its possible child paths are deleted
      * @since 1.0
      */
     static boolean delete(File path) {
         // if path is a directory, list and delete each found path
         if (path.isDirectory()) {
             for (File file : path.listFiles()) {
                 delete(file);
             }
         }

         // don't try to delete a non-existing file, we deem the result as true as the file does not exist
         return path.exists() ? path.delete() : true;
     }
     
}
