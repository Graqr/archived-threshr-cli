package com.graqr.threshr

import groovy.sql.Sql
import io.micronaut.configuration.picocli.PicocliRunner
import io.micronaut.context.ApplicationContext
import io.micronaut.context.annotation.Requires
import io.micronaut.context.annotation.Value
import io.micronaut.context.env.Environment
import org.spockframework.runtime.model.parallel.ExecutionMode
import spock.lang.AutoCleanup
import spock.lang.Execution
import spock.lang.Shared
import spock.lang.Unroll

@Requires(property = "test.datasources.default.url")
class ThreshrCliSpec extends ThreshrSpec {

    @Shared
    @Value('${test.datasources.default.url}')
    String url

    @Shared
    Sql sql

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

    void setupSpec() {
        sql = Sql.newInstance(url)
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

    @Execution(ExecutionMode.CONCURRENT)
    def "tcin arg can include #count values w/o errors"() {
        when:

        String[] tcinArg = sql.rows("select tcin FROM target_pdp TABLESAMPLE BERNOULLI (5) limit ${count}")
                .collect(row -> row.tcin)
        execute("--tcin", tcinArg.join(","), "--store-id", "1")

        then:
        errStream.toString().isBlank()

        then:


        where:
        count << (1..20)
    }

    def "tcin arg exceeding 20 count fails"() {
        when:

        String[] tcinArg = sql.rows("select tcin FROM target_pdp TABLESAMPLE BERNOULLI (${percent}) limit ${count}")
                .collect(row -> row.tcin)
        execute('--tcin', tcinArg.join(','), "--store-id", "1")
        def matcher = errStream.toString() =~ ".*maximum of 20 tcins allowed.*"

        then: "has expected error message"
        matcher.size() == 1


        where:
        count << (21..25)
        percent = (count/6).intValue() + 1 //db as 6k rows of tcin values
    }
}

