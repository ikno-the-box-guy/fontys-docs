<script setup lang="ts">
import {onBeforeMount, onMounted, ref} from "vue";
import {useRoute} from "vue-router";
import {Directory} from "../../entities/Directory.ts";
import axios from "axios";

const route = useRoute();

const directory = ref<Directory>();
const subdirectories = ref<Directory[]>();

const fetchDirectoryInfo = async () => {
  axios.get(import.meta.env.VITE_API_URL + "/directories/" + route.params.id)
  .then((response) => {
    directory.value = response.data;
  }).catch((error) => {
    console.error(error);
  });
}

const fetchSubdirectories = async () => {
  
}

const fetchDocuments = async () => {
  
}

onBeforeMount(() => {
  fetchSubdirectories();
  fetchDocuments();
});
</script>

<template>
  <h1>Explorer</h1>
  <h5>{{ directory }}</h5>
</template>

<style scoped>

</style>