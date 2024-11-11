import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import './index.css'
import router from "./router.ts";
import {createVfm} from "vue-final-modal";

const vfm = createVfm()

createApp(App).use(vfm).use(router).mount('#app')