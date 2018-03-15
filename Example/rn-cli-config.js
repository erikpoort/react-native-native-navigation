const path = require("path");
const config = {
	extraNodeModules: {
		"react": path.resolve(__dirname, "node_modules/react"),
		"react-native": path.resolve(__dirname, "node_modules/react-native"),
	},
	projectRoots: [
		path.resolve(__dirname),
		path.resolve(__dirname, "..")
	],
};
module.exports = config;