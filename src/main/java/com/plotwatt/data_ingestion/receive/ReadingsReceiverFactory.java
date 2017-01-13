package com.plotwatt.data_ingestion.receive;

/**
 * Created by liam on 1/13/17.
 */
public class ReadingsReceiverFactory {
    public static IReadingsReceiver createReadingsReceiver(String name) {
        // TODO it's probably worth being explicit here rather than doing reflective trickery.
        try {
            return (IReadingsReceiver)
                    Class.forName(ReadingsReceiverFactory.class.getPackage().getName() + ".impl." + name).newInstance();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
