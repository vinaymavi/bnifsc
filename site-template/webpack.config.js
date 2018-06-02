const merge = require("webpack-merge");
const parts = require("./webpack.part");

const HtmlWebpackPlugin = require("html-webpack-plugin");

const commonConfig = merge([
  {
    
    entry: {
      app: "./src/index.js"
    },
    output: {
      path: __dirname,
      filename: "./dist/[name].js"
    },
    module:{
        rules:[
            {
                test:/\.(jpg|png)$/,
                use:[{
                    loader:"file-loader",
                    options:{
                        name:"./dist/images/[name].[ext]"
                    }
                },
                {
                    loader:"image-webpack-loader"                    
                }
            ]
            }
        ]
    },
  },
  parts.loadJavaScript()
]);

const prodConfig = merge([
    parts.extractCSS({use:["css-loader",parts.autoprefixer(),"sass-loader"]})
]);

const devConfig = merge([
  parts.devServer({ host: process.env.HOST }, { port: process.env.PORT }),
  parts.loadCSS()
]);

module.exports = mode => {
    console.log(`MODE = ${mode}`);
  if (mode === "production") {
    console.log(JSON.stringify(merge(commonConfig, prodConfig, { mode })));
    return merge(commonConfig, prodConfig, { mode });
  } else {
    console.log(JSON.stringify(merge(commonConfig, devConfig, { mode })));
    return merge(commonConfig, devConfig, { mode });
  }
};
