package zio.rocksdb.service

import org.{ rocksdb => jrocks }
import zio.Task
import zio.stream.Stream

trait RocksDB {

  /**
   * Delete a key from the default ColumnFamily in the database.
   */
  def delete(key: Array[Byte]): Task[Unit]

  /**
   * Delete a key from a specific ColumnFamily in the database.
   */
  def delete(cfHandle: jrocks.ColumnFamilyHandle, key: Array[Byte]): Task[Unit]

  /**
   * Retrieve a key from the default ColumnFamily in the database.
   */
  def get(key: Array[Byte]): Task[Option[Array[Byte]]]

  /**
   * Retrieve a key from a specific ColumnFamily in the database.
   */
  def get(cfHandle: jrocks.ColumnFamilyHandle, key: Array[Byte]): Task[Option[Array[Byte]]]

  /**
   * Retrieves the list of ColumnFamily handles the database was opened with.
   *
   * Caveats:
   * - This list will only be populated if the database was opened with a specific list of
   *   column families.
   * - The list will not be updated if column families are added/removed while the database
   *   is open.
   */
  def initialHandles: Task[List[jrocks.ColumnFamilyHandle]]

  /**
   * Retrieve multiple keys from the default ColumnFamily in the database. The resulting list
   * corresponds (positionally) to the list of keys passed to the function.
   */
  def multiGetAsList(keys: List[Array[Byte]]): Task[List[Option[Array[Byte]]]]

  /**
   * Retrieve multiple keys from specific ColumnFamilies in the database. The resulting list
   * corresponds (positionally) to the list of keys passed to the function.
   */
  def multiGetAsList(
    handles: List[jrocks.ColumnFamilyHandle],
    keys: List[Array[Byte]]
  ): Task[List[Option[Array[Byte]]]]

  /**
   * Scans the default ColumnFamily in the database and emits the results as a `ZStream`.
   */
  def newIterator: Stream[Throwable, (Array[Byte], Array[Byte])]

  /**
   * Scans a specific ColumnFamily in the database and emits the results as a `ZStream`.
   */
  def newIterator(cfHandle: jrocks.ColumnFamilyHandle): Stream[Throwable, (Array[Byte], Array[Byte])]

  /**
   * Scans multiple ColumnFamilies in the database and emits the results in multiple streams,
   * whereas the streams themselves are also emitted in a `ZStream`.
   */
  def newIterators(
    cfHandles: List[jrocks.ColumnFamilyHandle]
  ): Stream[Throwable, (jrocks.ColumnFamilyHandle, Stream[Throwable, (Array[Byte], Array[Byte])])]

  /**
   * Writes a key to the default ColumnFamily in the database.
   */
  def put(key: Array[Byte], value: Array[Byte]): Task[Unit]

  /**
   * Writes a key to a specific ColumnFamily in the database.
   */
  def put(cfHandle: jrocks.ColumnFamilyHandle, key: Array[Byte], value: Array[Byte]): Task[Unit]
}
