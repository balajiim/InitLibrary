package com.venue.venuewallet.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mastmobilemedia on 08-12-2017.
 */

public class AuthorizeToken {
    /*  public String getAccess_token() {
          return access_token;
      }

      public void setAccess_token(String access_token) {
          this.access_token = access_token;
      }

      @SerializedName("access_token")
      @Expose
      private String access_token;

      @SerializedName("token_type")
      @Expose
      private String token_type;

      public String getToken_type() {
          return token_type;
      }

      public void setToken_type(String token_type) {
          this.token_type = token_type;
      }*/
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;
    @SerializedName("token_type")
    private String tokenType;
    @SerializedName("expires_in")
    private int expiresIn;

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public int getExpiresIn() {
        return expiresIn;
    }
}
