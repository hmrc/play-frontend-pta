# play-frontend-pta

This library contains a number of Twirl 
components needed for HMRC Personal Tax Account (PTA) frontend microservices.

This library is compatible with Play Framework version 2.8 or above only.

## Usage
1. Add as a dependency in your AppDependencies.scala

    ```sbt
    libraryDependencies += "uk.gov.hmrc" %% "play-frontend-pta" % "X.Y.Z"
    ```
   
2. Add PtaHead in the additionalHeadBlock parameter of hmrcLayout

    ```scala
    additionalHeadBlock = Some(ptaHead())
   ```

3. Add PtaScripts in the additionalScriptsBlock parameter of hmrcLayout

    ```scala
    additionalScriptsBlock = Some(ptaScripts())
    ```

4. Add a route in your app.routes

    ```scala
    ->          /pta-frontend                                ptafrontend.Routes
    ```

5. Use components as required. They live in the package `uk.gov.hmrc.ptafrontend.views.html`. For example,

    ```scala
        @import uk.gov.hmrc.ptafrontend.views.html.PtaAttorneyBanner
        @import uk.gov.hmrc.ptafrontend.viewmodels.attorneybanner.AttorneyBanner

        @attorneyBanner = {
          @ptaAttorneyBanner(AttorneyBanner(name = "Joe Bloggs", classes = "", accountUrl = "/url-to-account"))
        }
   
        @hmrcLayout(
          pageTitle = Some(pageTitle),
          nonce = CSPNonce.get,
          additionalBannersBlock = Some(attorneyBanner)
        )(contentBlock)
    ```

## Attorney Banner

The Attorney Banner adds a persistent floating banner to display additional login information.

If you have a need to customise the content used within this component, you can override the following message keys
in your service. For example,

```scala
attorney.banner.link=Return to your own account
attorney.banner.using.service.for=You are in <span class="govuk-!-font-weight-bold">{0}’s</span> account.
```

Three modifier classes are provided that can be supplied in the `classes` parameter:

| Modifier class | Usage  |
| ---------------| ------ |
|.pta-attorney-banner--client-request-accepted|If agent has accepted the client request|
|.pta-attorney-banner--client-request-requested|When client has requested an agent, but the agent hasn't accepted the client request yet. its pending|
|.pta-attorney-banner--client-request-rejected|When an agent rejects a client request to act on his/her behalf|

The above classes were copied across from the assets-frontend/play-ui implementation. If it turns out these are not used,
maintainers should consider removing them in the interests of simplicity and removal of dead code.

## Maintenance

### Prerequisites

You will need Node.js v14 or above installed locally.

### Linting and auto-formatting

We are using eslint with the Airbnb configuration for Javascript linting and Scalafmt for Scala formatting.

It's a good idea to set up your IDE with the provided [eslint](.eslintrc.json) and [scalafmt](.scalafmt.conf) configuration and 
turn on format on save for both tools.

If you are using Intellij IDEA, further information can be found below:

* https://www.jetbrains.com/help/idea/work-with-scala-formatter.html
* https://www.jetbrains.com/help/idea/eslint.html

### Running all Scala and Javascript checks

```
sbt test
```

### Publishing locally for testing with frontend microservices

```
sbt publishLocal
```

Once published locally, append `-SNAPSHOT` to your dependency in `AppDependencies.scala`

## License

This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html").
