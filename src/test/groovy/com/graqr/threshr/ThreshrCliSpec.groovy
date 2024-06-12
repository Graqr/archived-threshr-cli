package com.graqr.threshr

import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.env.Environment
import spock.lang.AutoCleanup
import spock.lang.Shared

class ThreshrCliSpec extends ThreshrSpec {

    @Shared
    final PrintStream originalOut = System.out
    @Shared
    final PrintStream originalErr = System.err

    @Shared
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream()
    ByteArrayOutputStream errStream = new ByteArrayOutputStream()


    @Shared
    @AutoCleanup
    ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)

    void execute(String... args) {
        PicocliRunner.run(ThreshrCli, ctx, args)
    }

    def setup() {
        outputStream.reset()
        errStream.reset()
        System.setOut(new PrintStream(outputStream))
        System.setErr(new PrintStream(errStream))
    }

    def cleanup() {
        System.setOut(originalOut)
        System.setErr(originalErr)
    }

    def "cli help can be queried successfully"() {
        when:
        execute('--help')

        then:
        outputStream.toString() ==
                "Usage: threshr grocery query tool [-hV] [-t=<tcinValues>]...\n" +
                "  -h, --help      Show this help message and exit.\n" +
                "  -t, --tcin, product-id-number=<tcinValues>\n" +
                "\n" +
                "  -V, --version   Print version information and exit.\n"

    }
}
