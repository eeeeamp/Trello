package base;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

public class MyTestWatcher implements TestWatcher {

    public static String path;
    public static String id;

    @Override
    public void testFailed(ExtensionContext extensionContext, Throwable throwable) {

        if (path != null & id != null) {

            System.out.println("Unnecessary resource was deleted");
            BaseTest.deleteResource(path, id);

        }

    }

    @Override
    public void testSuccessful(ExtensionContext extensionContext) {

        BaseTest.deleteResource(path, id);

    }

}
