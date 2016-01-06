package weibo4j;

import java.io.Serializable;
import weibo4j.http.HttpClient;
import weibo4j.http.Response;
import weibo4j.model.IDs;
import weibo4j.model.Paging;
import weibo4j.model.PostParameter;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.User;
import weibo4j.util.WeiboConfig;

public class Weibo
  implements Serializable
{
  private static final long serialVersionUID = 4282616848978535016L;
  public static String CONSUMER_KEY = "";
  public static String CONSUMER_SECRET = "";
  public static String REDIRECT_URI = "";
  public static HttpClient client = new HttpClient();
  
  public synchronized void setToken(String token)
  {
    client.setToken(token);
  }
  
  public IDs getFriendsIDSByUserId(String userid, Integer count)
    throws weibo4j.model.WeiboException
  {
    return new IDs(client.get(WeiboConfig.getValue("baseURL") + "friendships/followers/ids.json", new PostParameter[] { new PostParameter("uid", userid), new PostParameter("count", count.toString()) }), this);
  }
  
  public IDs getFriendsIDSByUserId(String uid, Integer count, Integer cursor)
    throws weibo4j.model.WeiboException
  {
    return new IDs(client.get(WeiboConfig.getValue("baseURL") + "friendships/friends/ids.json", new PostParameter[] { new PostParameter("uid", uid), new PostParameter("count", count.toString()), new PostParameter("cursor", cursor.toString()) }), this);
  }
  
  public IDs getFriendsBilateral(String userid, Integer count)
    throws weibo4j.model.WeiboException
  {
    return new IDs(client.get(WeiboConfig.getValue("baseURL") + "friendships/friends/bilateral/ids.json", new PostParameter[] { new PostParameter("uid", userid), new PostParameter("count", count.toString()) }), this);
  }
  
  public IDs getFriendsIDSByUserId(String userid)
    throws weibo4j.model.WeiboException
  {
    return new IDs(client.get(WeiboConfig.getValue("baseURL") + "friendships/friends/ids.json", new PostParameter[] { new PostParameter("uid", userid) }), this);
  }
  
  public StatusWapper getFriendsTimeline3(String userid, String max_id)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return Status.constructWapperStatus(client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", userid), new PostParameter("max_id", max_id) }));
  }
  
  public IDs getFollowsIDSByUserId(String userid, Paging paging)
    throws weibo4j.model.WeiboException
  {
    return new IDs(client.get(WeiboConfig.getValue("baseURL") + "friendships/followers/ids.json", new PostParameter[] { new PostParameter("uid", userid) }, paging), this);
  }
  
  public User showUserByName(String uname)
    throws weibo4j.model.WeiboException, WeiboException
  {
    return new User(client.get(WeiboConfig.getValue("baseURL") + "users/show.json", new PostParameter[] { new PostParameter("uid", uname) }).asJSONObject());
  }
  
  public User showIdByDomain(String domain)
    throws weibo4j.model.WeiboException, WeiboException
  {
    return new User(client.get(WeiboConfig.getValue("baseURL") + "users/domain_show.json", new PostParameter[] { new PostParameter("domain", domain) }).asJSONObject());
  }
  
  public Response showmid(String mid, int type)
    throws weibo4j.model.WeiboException, WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/querymid.json", new PostParameter[] { new PostParameter("mid", mid), new PostParameter("type", type) });
  }
  
  public Response showid(String mid, int type)
    throws weibo4j.model.WeiboException, WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/queryid.json", new PostParameter[] { new PostParameter("mid", mid), new PostParameter("type", type) });
  }
  
  public Response shorturlTolongurl(String shorturl, int type)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "short_url/expand.json", new PostParameter[] { new PostParameter("url_short", shorturl) });
  }
  
  public Response getStatusesById(String mid)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/show.json", new PostParameter[] { new PostParameter("id", mid) });
  }
  
  public Response getPublicPlace(int count)
    throws weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/public_timeline.json", new PostParameter[] { new PostParameter("count", count) });
  }
  
  public Response getFriendsTimeline2(String userid, String max_id)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", userid), new PostParameter("max_id", max_id) });
  }
  
  public Response getFriendsTimeline4(String userid, String since_id)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", userid), new PostParameter("since_id", since_id) });
  }
  
  public Response getUserBatch(String uid)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/show_batch.json", new PostParameter[] { new PostParameter("ids", uid) });
  }
  
  public Response getUserTimelineByUidTest(String uid, Paging page)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", uid) }, page);
  }
  
  public Response getUserTimelineByUidTest(String uid, Paging page, Long maxId)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", uid), new PostParameter("maxId", maxId.longValue()) }, page);
  }
  
  public StatusWapper getUserTimelineByUid(String uid, Paging page)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return Status.constructWapperStatus(client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("screen_name", uid) }, page));
  }
  
  public StatusWapper getFriendsTimeline(String userid, String since_id)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return Status.constructWapperStatus(client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", userid), new PostParameter("since_id", since_id) }));
  }
  
  public Response getPlaceByUid(String uid, Paging page)
    throws weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "place/user_timeline.json", new PostParameter[] { new PostParameter("uid", uid) }, page);
  }
  
  public Response showIffriend(String source, String target)
    throws weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "friendships/show.json", new PostParameter[] { new PostParameter("source_id", source), new PostParameter("target_id", target) });
  }
  
  public User showUserById(String uname)
    throws weibo4j.model.WeiboException, WeiboException
  {
    return new User(client.get(WeiboConfig.getValue("baseURL") + "users/show.json", new PostParameter[] { new PostParameter("screen_name", uname) }).asJSONObject());
  }
  
  public StatusWapper getRepostTimeline(String mid, Paging page)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return Status.constructWapperStatus(client.get(WeiboConfig.getValue("baseURL") + "statuses/repost_timeline.json", new PostParameter[] { new PostParameter("id", mid) }, page));
  }
  
  public Response getRepostTimeline1(String mid, Paging paging)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/repost_timeline.json", new PostParameter[] { new PostParameter("id", mid) }, paging);
  }
  
  public Response getCommentTimeline1(String mid, Paging paging)
    throws WeiboException, weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "comments/show.json", new PostParameter[] { new PostParameter("id", mid) }, paging);
  }
  
  public Response getUserStream(String uid, Paging page)
    throws weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/user_timeline.json", new PostParameter[] { new PostParameter("uid", uid) }, page);
  }
  
  public Response getGlobalStream(int count)
    throws weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/public_timeline.json", new PostParameter[] { new PostParameter("count", count) });
  }
  
  public Response getRepostStream(String mid, Paging paging)
    throws weibo4j.model.WeiboException
  {
    return client.get(WeiboConfig.getValue("baseURL") + "statuses/repost_timeline.json", new PostParameter[] { new PostParameter("id", mid) }, paging);
  }
}
