package com.graqr.threshr

import com.graqr.threshr.model.queryparam.Place
import com.graqr.threshr.model.queryparam.TargetStorePdpSearch
import com.graqr.threshr.model.redsky.products.pdp.client.PdpClientRoot
import com.graqr.threshr.model.redsky.products.summary.ProductSummaryRoot
import com.graqr.threshr.model.redsky.stores.NearbyStoresRoot
import io.micronaut.http.HttpResponse

import java.util.stream.Collectors
/**
 * This test class is necessary despite similarity to the controller test. please don't delete this as the
 * httpclient logs are visible in this test but not in the controller test.
 */
class ThreshrClientSpec extends ThreshrSpec {

    void "no error requesting product summaries"() {
        when:
        HttpResponse<ProductSummaryRoot> response = threshrClient.productSummaryWithFulfillment(
                targetStore, tcin)

        then:
        noExceptionThrown()
        response.body().data().productSummary().size() == tcin.tcins.split(",").size()
    }

    void "no error calling pdp client search"() {
        when:
        HttpResponse<PdpClientRoot> response = threshrClient.productDetails(
                new TargetStorePdpSearch(targetStore),
                tcin.getTcins().split(",")[0])

        then:
        noExceptionThrown()
        null != response.body().data().product()
    }

    void 'querying "#place.getPlace()" returns the "#expectedLocationName" store'() {
        when:
        HttpResponse<NearbyStoresRoot> response = threshrClient.queryNearbyStores(5,100, place.getPlace())

        then:
        response.body().data().nearbyStores().stores()
                .stream()
                .map(it -> it.locationName())
                .collect(Collectors.toList())
                .contains(expectedLocationName)

        where:
        store << expectedStores
        expectedLocationName = store.locationName()
        place = 0 == new Random().nextInt(2)
                ? new Place(store.mailingAddress().postalCode())
                : new Place(store.mailingAddress().city(), store.mailingAddress().state())
    }

}
