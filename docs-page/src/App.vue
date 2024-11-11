<script setup lang="ts">
import NavBar from "./components/NavBar.vue";
import {ModalsContainer} from "vue-final-modal";
import 'vue-final-modal/style.css'
</script>

<template>
  <div class="flex flex-col h-full">
    <NavBar />
    <div class="grow">
      <RouterView/>
    </div>
  </div>
  
  <ModalsContainer/>
</template>

<style scoped>

</style>
