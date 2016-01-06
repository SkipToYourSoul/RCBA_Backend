package local.crawler.control;

import weibo4j.model.Status;

/**
 * 根据tweets当前的状态，判断它未来的走势
 * 
 * @author xiafan
 *
 */
public class TweetLifeCycleEvaluator {
	public static final float THRESHOLD = 0.4f;

	/**
	 * 根据微博的生命周期和转发量，判断它的热度
	 * 
	 * @param status
	 * @return
	 */
	public static float evaluate(Status status, float velocity) {
		if (status.getRetweetedStatus() != null)
			return 0f;

		float timeGap = (float) ((System.currentTimeMillis() - status.getCreatedAt().getTime()) / (1000 * 3600) + 1);
		return (float) (status.getRepostsCount() / timeGap * 0.3 + 0.7 * velocity);
	}

	public static boolean isHotTweet(Status status, float velocity) {
		if (evaluate(status, velocity) > 10.0f) {
			return true;
		}
		return false;
	}
}
