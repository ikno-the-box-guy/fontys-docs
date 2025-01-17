import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import './index.css'
import { router } from "./router.ts";
import {createVfm} from "vue-final-modal";

const vfm = createVfm()

const app = createApp(App)

app.directive('focus', {
    mounted(el) {
        el.focus()
    }
})
    
app.use(vfm).use(router).mount('#app')