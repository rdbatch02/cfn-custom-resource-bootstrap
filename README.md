# cfn-custom-resource-bootstrap ![Build](https://github.com/rdbatch02/cfn-custom-resource-bootstrap/workflows/Build/badge.svg)[ ![Download](https://api.bintray.com/packages/c1phr/com.batchofcode/cfn-custom-resource-bootstrap/images/download.svg) ](https://bintray.com/c1phr/com.batchofcode/cfn-custom-resource-bootstrap/_latestVersion)
Bootstrapping library for creating Cloudformation Custom Resources

This library is distributed through Bintray: [https://bintray.com/c1phr/com.batchofcode/cfn-custom-resource-bootstrap](https://bintray.com/c1phr/com.batchofcode/cfn-custom-resource-bootstrap)

## Usage

Import the library into a Gradle Project:

```kotlin
repositories {
    //...
    maven { url = uri("https://dl.bintray.com/c1phr/com.batchofcode") }
}

dependencies {
  // ...
  implementation("com.batchofcode:cfn-custom-resource-bootstrap:${version}")
}
```

Setup a class that implements `CustomResource` and implement the required handler methods. These methods correspond to the phases of a Cloudformation deployment.

```kotlin
import com.batchofcode.cfn.CustomResource

class MyCustomResource: CustomResource {
    override fun onCreate(request: CfnRequest, context: Context?): Response {
        return Response(
                Status = ResponseStatus.SUCCESS,
                Reason = null,
                NoEcho = false,
                Data = responsePayload
            )
    }

    override fun onDelete(request: CfnRequest, context: Context?): Response {
        return Response.emptySuccess()
    }

    override fun onUpdate(request: CfnRequest, context: Context?): Response {
        return Response(
                Status = ResponseStatus.FAILED,
                Reason = "Missing data",
                NoEcho = false,
                Data = responsePayload
            )
    }
}
```

Deploy a lambda with the handler specified as `com.batchofcode.cfn.CustomResourceHandler::handleRequest`, then set the environment variable `CUSTOM_RESOURCE_HANDLER` to reference your handler class: `"CUSTOM_RESOURCE_HANDLER_CLASS": "com.package.MyCustomResource"`.
