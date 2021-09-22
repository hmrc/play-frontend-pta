const path = require('path');

module.exports = {
  entry: {
    pta: './js/pta.js'
  },
  module: {
    rules: [
      {
        test: /\.js?$/,
        use: ['babel-loader'],
        exclude: /node_modules/,
      }
    ],
  },
  output: {
    path: path.resolve(__dirname, 'src/main/resources/assets'),
  },
};
