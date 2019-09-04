package com.wsoteam.diet.di;

import ru.terrakok.cicerone.Cicerone;
import ru.terrakok.cicerone.NavigatorHolder;
import ru.terrakok.cicerone.Router;

public class CiceroneModule {

  private static Cicerone<Router> cicerone = Cicerone.create();

  public static Cicerone instance() {
    return cicerone;
  }

  public static NavigatorHolder navigator() {
    return cicerone.getNavigatorHolder();
  }

  public static Router router() {
    return cicerone.getRouter();
  }
}
