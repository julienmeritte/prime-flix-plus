const { createProxyMiddleware } = require("http-proxy-middleware");

module.exports = function (app) {
  if (process.env.DEV === "true") {
    app.use(
      "/api",
      createProxyMiddleware({
        target: "http://localhost:8080/",
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      })
    );
  } else {
    app.use(
      "/api",
      createProxyMiddleware({
        target: "http://api:8080/",
        changeOrigin: true,
        pathRewrite: {
          "^/api": "",
        },
      })
    );
  }
};
