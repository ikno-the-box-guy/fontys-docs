<script setup lang="ts">
import NavBar from "./components/NavBar.vue";
</script>

<template>
  <div class="flex flex-col h-full">
    <NavBar />
    <RouterView/>
  </div>
</template>

<style scoped>

</style>
