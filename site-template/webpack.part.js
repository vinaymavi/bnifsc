const MiniCSSExtractPlugin = require("mini-css-extract-plugin");
exports.devServer = ({ host, port }) => ({
  devServer: {
    stats: "errors-only",
    host,
    port,
    open: true,
    overlay: true
  }
});

exports.loadCSS = ({ include, exclude } = {}) => ({
  module: {
    rules: [
      {
        test: /\.scss$/,
        include,
        exclude,
        use: ["style-loader", "css-loader", "sass-loader"]
      }
    ]
  }
});

exports.extractCSS = ({ include, exclude, use = [] }) => {
  //Output extracted CSS to file.
  const plugin = new MiniCSSExtractPlugin({
    filename: "./dist/[name].css"
  });

  return {
    module: {
      rules: [
        {
          test: /\.scss$/,
          include,
          exclude,
          use: [MiniCSSExtractPlugin.loader].concat(use)
        }
      ]
    },
    plugins: [plugin]
  };
};
// TODO autoprefixer not workint with scss.
exports.autoprefixer = () => ({
  loader: "postcss-loader",
  options: {
    plugins: () => [require("autoprefixer")]
  }
});

exports.loadJavaScript = ({ include, exclude } = {}) => {
  return {
    module: {
      rules: [
        {
          test: /\.(js|jsx)$/,
          use: ["babel-loader"]
        }
      ]
    }
  };
};
