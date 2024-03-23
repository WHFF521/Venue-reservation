//接口config文件 by cwj
const baseUrl = "http://localhost:8888/";
const apiconfig = {
	dev: {
		baseUrl: 'http://localhost:8888'
	},
	prod: {
		baseUrl: 'http://:8888'
	}
}
export default {
	apiconfig,
	baseUrl,
}