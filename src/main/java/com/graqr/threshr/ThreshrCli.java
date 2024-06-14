package com.graqr.threshr;

import com.graqr.threshr.model.queryparam.TargetStore;
import com.graqr.threshr.model.queryparam.Tcin;
import com.graqr.threshr.model.redsky.store.Store;
import io.micronaut.configuration.picocli.PicocliRunner;
import jakarta.inject.Singleton;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Singleton
@Command(name = "threshr grocery query tool", mixinStandardHelpOptions = true)
public class ThreshrCli implements Runnable {

    private final ThreshrController controller;
    @Option(names = {"--tcin", "-t"},
            required = true,
            description = "",
            converter = TcinsConverter.class)
    Tcin tcinValues;

    @Option(names = {"--store-id", "-s"},
            required = true,
            description = "store id as given in redsky api",
            converter = TargetStoreConverter.class)
    TargetStore storeId;

    public ThreshrCli(ThreshrController threshr) {
        this.controller = threshr;
    }


    public static void main(String[] args) {
        PicocliRunner.run(ThreshrCli.class, args);
    }

    public void run() {
        //do all the things
    }


    static class TargetStoreConverter implements CommandLine.ITypeConverter<TargetStore> {
        ThreshrController threshrController;

        public TargetStoreConverter(ThreshrController threshrController) {
            this.threshrController = threshrController;
        }

        @Override
        public TargetStore convert(String s) throws ThreshrException {
            Store store = threshrController.getStore(s);
            return new TargetStore(store.storeId(),
                    store.mailingAddress().state(),
                    store.mailingAddress().postalCode(),
                    store.geographicSpecifications().latitude(),
                    store.geographicSpecifications().longitude());

        }
    }

    static class TcinsConverter implements CommandLine.ITypeConverter<Tcin> {
        @Override
        public Tcin convert(String s) throws ThreshrException {
            return new Tcin(s.split(","));
        }
    }
}