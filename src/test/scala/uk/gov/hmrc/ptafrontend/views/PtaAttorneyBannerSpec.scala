/*
 * Copyright 2023 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.ptafrontend.views

import org.jsoup.Jsoup
import play.api.i18n.{Messages, MessagesApi}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import uk.gov.hmrc.ptafrontend.{RequestHelpers, SpecBase}
import uk.gov.hmrc.ptafrontend.viewmodels.attorneybanner.AttorneyBanner
import uk.gov.hmrc.ptafrontend.views.html.PtaAttorneyBanner

class PtaAttorneyBannerSpec extends SpecBase {
  "PtaAttorneyBanner" should {
    "render standard text" in new Fixture {
      val html = ptaAttorneyBanner(
        AttorneyBanner(
          name = "James Smith",
          accountUrl = "#",
          classes = ""
        )
      )

      val parsed = Jsoup.parse(html.body)
      parsed.select(".pta-attorney-banner__text").text shouldBe "You are using this service for James Smith."
    }

    "render a link" in new Fixture {
      val html = ptaAttorneyBanner(
        AttorneyBanner(
          name = "Anyone",
          accountUrl = "/account/james-smith",
          classes = ""
        )
      )

      val parsed = Jsoup.parse(html.body)
      parsed.select(".pta-attorney-banner__link").text         shouldBe "Return to your account"
      parsed.select(".pta-attorney-banner__link").attr("href") shouldBe "/account/james-smith"
    }
  }

  "render custom classes" in new Fixture {
    val html = ptaAttorneyBanner(
      AttorneyBanner(
        name = "Anyone",
        accountUrl = "#",
        classes = "pta-attorney-banner--client-request-rejected"
      )
    )

    val parsed = Jsoup.parse(html.body)
    parsed.select(".pta-attorney-banner").attr("class") should include("pta-attorney-banner--client-request-rejected")
  }

  "escape any injected HTML in name" in new Fixture {
    val html = ptaAttorneyBanner(
      AttorneyBanner(
        name = "<script src='https://example.com/really-bad.js'></script>",
        accountUrl = "#",
        classes = "pta-attorney-banner--client-request-rejected"
      )
    )

    val parsed = Jsoup.parse(html.body)
    parsed
      .select(".pta-attorney-banner__text")
      .html shouldBe "You are using this service for <span class=\"govuk-!-font-weight-bold\">&lt;script src='https://example.com/really-bad.js'&gt;&lt;/script&gt;</span>."
  }

  trait Fixture extends RequestHelpers {
    implicit lazy val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest("GET", "/foo")
    implicit lazy val messagesApi: MessagesApi                         = app.injector.instanceOf[MessagesApi]
    implicit lazy val messages: Messages                               = messagesApi.preferred(fakeRequest)
    val ptaAttorneyBanner                                              = new PtaAttorneyBanner()
  }
}
