package com.revolut.assignement.pulkit.common;

import com.google.inject.Singleton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

@Singleton
public class CommonsUtil {

  public static String getExternalReferenceId(final Long referenceId) {
    DateFormat df = new SimpleDateFormat("yyyyMMddHH");
    String incrementString = String.valueOf(referenceId % 10000000L);
    String rndmStrng =
        RandomStringUtils.random(7 - incrementString.replaceFirst("^0+(?!$)", "").length(), "ABCD");
    return StringUtils.join("91", "4", df.format(new Date()), rndmStrng, incrementString);
  }
}
