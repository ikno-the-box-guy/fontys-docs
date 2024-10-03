<script setup lang="ts">
import user from "../utils/localUser.ts";
import {ref} from "vue";

const rootUrl = ref('/explorer/' + user.value.root);
</script>

<template>
  <h1>Home</h1>
  
  <RouterLink class="btn btn-blue" :to='rootUrl'>go to root</RouterLink>
</template>

<style scoped>

</style>