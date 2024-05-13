# natchez-server-logger-repro-example

Example repo to demonstrate http4s Server Logger middleware
is incompatible with loggers using IOLocal context, e.g. otel4s or natchez trace
context

To run:

```
env $(cat local.env) sbt run
```

## Issue

We can see that natchez-log and the implementation of `TracedLogger` match up,
with log statements in the app including correct trace IDs. The Client Logger
also correctly applies trace IDs in the mdc, using the same `logAction`

With the client we see that order of application of tracing middleware and
logging middleware matters to whether trace headers appear in the logged request headers, but not to whether they appear in the log statement's MDC.

However the server logger does not apply any trace context, regardless of any
order of application of middleware.

## Desired Outcome

When creating the `http4s-server-request` span, the logged request body message should
include the Trace ID and Span ID in the MDC
