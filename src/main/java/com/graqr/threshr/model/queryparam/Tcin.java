package com.graqr.threshr.model.queryparam;

import com.graqr.threshr.ThreshrException;
import io.micronaut.core.annotation.Introspected;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Introspected
public class Tcin {

    String[] tcins;

    public Tcin(String... tcin) throws ThreshrException {
        setTcins(tcin);
    }

    /**
     * checks if the provided string is appropriately sized and only digits prior to assigning the field. multiple
     * args can only be passed in quantities up to 20.
     *
     * @param tcins var arg of String TCIN values. See
     *              <a href="https://partners.target.com/termsandconditions">partners agreement</a> under
     *              "Merchandiser Datafeed"
     */
    public void setTcins(String... tcins) throws ThreshrException {
        if (tcins.length > 20) {
            throw new ThreshrException( "maximum of 20 tcins allowed");
        }
        Predicate<String> tcinCheck = t -> !String.valueOf(t).matches("\\d{5,9}");
        String badTcin = Arrays.stream(tcins)
                .filter(tcinCheck)
                .collect(Collectors.joining(", "));
        if (!badTcin.isEmpty()) {
            throw new ThreshrException( "the following are invalid tcin:" + badTcin);
        }
        this.tcins = tcins;
    }



    public String getTcins() {
        return Arrays.stream(tcins)
                .map(Objects::toString)
                .collect(Collectors.joining(","));
    }
}