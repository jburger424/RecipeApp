package edu.ithaca.recipeApp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DatabaseConnectTest {
  DatabaseConnect db = new DatabaseConnect();
  @Test
  void logInUser() {
    Assert.assertEquals(true, db.logInUser("jon","1234"));
    Assert.assertEquals(false, db.logInUser("jon","12345"));
  }

  @Test
  void userExists() {
    Assert.assertEquals(true, db.userExists("jon"));
    Assert.assertEquals(false, db.userExists("dtrump"));
  }

  @Test
  void addUser() {
    Assert.assertEquals(false, db.addUser("jon","1234"));
  }

}