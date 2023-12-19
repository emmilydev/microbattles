package online.nasgar.microbattles.api.credentials;

import java.beans.ConstructorProperties;

/**
 * Represents the credentials for a
 * remote server.
 */
public class ServerCredentials {
  private final String host;
  private final int port;
  private final String username;
  private final String password;
  private final String database;
  private final boolean authEnabled;

  @ConstructorProperties({
    "host",
    "port",
    "authEnabled",
    "username",
    "password",
    "database"
  })
  public ServerCredentials(String host,
                           int port,
                           boolean authEnabled,
                           String username,
                           String password,
                           String database) {
    this.host = host;
    this.port = port;
    this.authEnabled = authEnabled;
    this.username = username;
    this.password = password;
    this.database = database;
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public boolean isAuthEnabled() {
    return authEnabled;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public String getDatabase() {
    return database;
  }
}
