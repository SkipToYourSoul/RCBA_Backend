/**
 * The first thing to know about are types. The available types in Thrift are:
 *
 *  bool        Boolean, one byte
 *  byte        Signed byte
 *  i16         Signed 16-bit integer
 *  i32         Signed 32-bit integer
 *  i64         Signed 64-bit integer
 *  double      64-bit floating point value
 *  string      String
 *  binary      Blob (byte array)
 *  map<t1,t2>  Map from one type to another
 *  list<t1>    Ordered list of one type
 *  set<t1>     Set of unique elements of one type
 *
 * Did you also notice that Thrift supports C style comments?
 */

// Just in case you were wondering... yes. We support simple C comments too.

/**
 * Thrift files can namespace, package, or prefix their output in various
 * target languages.
 */
namespace java kafka.state

struct RepostCrawlState {
	1: string mid,
	2: i64 sinceID,
	3: i64 lastCrawlTime,
	4: i16 lastCrawlNum
}

struct UserCrawlState{
	1: string uid,
	2: i64 sinceID,
	3: i64 lastCrawlTime,
	4: i16 lastCrawlNum
}

struct TimeSeriesUpdateState {
	1: string mid,
	2: i64 timestamp,
	3: bool degrade
}
