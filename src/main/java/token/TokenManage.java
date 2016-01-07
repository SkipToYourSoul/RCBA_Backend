package token;

import local.crawler.control.Configuration;
import local.crawler.control.Controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.Properties;

/**
 * Manage the token pool
 *
 * @author liye
 */
public class TokenManage {
    static TokensPool tokenspool = null;
    int maxCount = 1000;

    public TokenManage() {
        tokenspool = new TokensPool(Controller.configuration.getTokenFile());
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public Token GetToken() {
        Token tokenPack = null;
        tokenPack = tokenspool.getTokenList().get(0);
        return tokenPack;
    }

    //get next token in token list
    public synchronized Token GetNextToken() {
        Token tokenPack = null;
        int nextPos = tokenspool.getPos() + 1;
        tokenspool.setPos(nextPos);
        if (tokenspool.IsOver()) {
            tokenspool.setPos(0);
        }
        tokenPack = (Token) tokenspool.getTokenList().get(tokenspool.getPos());
        if (tokenPack == null) {
            System.out.println("Token error");
            return null;
        }
        tokenPack.setCount(0);
        System.out.println("Change Token : " + tokenPack.token);

        return tokenPack;
    }

    //judge if token count is max
    public boolean maxTokenCount(Token token) {
        if (token.getCount() > this.maxCount) {
            return false;
        }
        return true;
    }

    //refresh token in local files
    public static boolean refreshToken() {
        GetAccessToken wc = new GetAccessToken();
        try {
            wc.run();
            System.out.println("refresh token finished");
        } catch (IOException e) {
            return false;
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
