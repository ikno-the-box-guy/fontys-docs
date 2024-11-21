<script setup lang="ts">
import {onBeforeMount, ref, watch} from "vue";
import {useRoute} from "vue-router";
import {Directory} from "../../entities/Directory.ts";
import {Document} from "../../entities/Document.ts";
import DirectoryList from "../../components/explorer/DirectoryList.vue";
import DocumentList from "../../components/explorer/DocumentList.vue";
import {documentApi} from "../../api/AxiosInstances.ts";

const route = useRoute();

const directory = ref<Directory>();
const directories = ref<Directory[]>();
const documents = ref<Document[]>();

const fetchDirectoryInfo = async () => {
  documentApi.get("/directories/" + route.params.directory)
  .then((response) => {
    directory.value = response.data as Directory;
  }).catch((error) => {
    console.error(error);
  });
}

const fetchSubdirectories = async () => {
  documentApi.get("/explorer/" + route.params.directory + "/directories")
  .then((response) => {
    directories.value = response.data as Directory[];
  }).catch((error) => {
    console.error(error);
  });
}

const fetchDocuments = async () => {
  documentApi.get("/explorer/" + route.params.directory + "/documents")
  .then((response) => {
    documents.value = response.data as Document[];
  }).catch((error) => {
    console.error(error);
  });
}

onBeforeMount(() => {
  fetchDirectoryInfo();
  fetchSubdirectories();
  fetchDocuments();
});

watch(route, () => {
  fetchDirectoryInfo();
  fetchSubdirectories();
  fetchDocuments();
});
</script>

<template>
 <div class="p-6 min-h-full xl:px-32">
<!--    <h1 class="text-3xl font-bold mb-4">Explorer</h1>-->

    <div v-if="directory && directory.id !== directory.parentId" class="mb-6 text-3xl flex ml-6">
      <RouterLink :to="'/explorer/' + directory.parentId" class="text-blue-600 hover:underline flex items-center">
        ../
      </RouterLink>
      <span class="font-semibold text-gray-800">{{ directory.displayName }}</span>

<!--      <h2 class="text-xl font-semibold text-gray-800">{{ directory.displayName }}</h2>-->
    </div>
   
   <div v-if="directory && directory.id === directory.parentId" class="mb-6 text-3xl text-gray-400 ml-6">
     ~/
   </div>

    <div class="flex flex-col md:flex-row w-full space-y-4 md:space-x-4 md:space-y-0">
      <div class="flex-col w-full md:w-1/2 rounded-lg border-2 p-4">
        
        <div v-if="directories && directory" class="h-full">
          <DirectoryList :parent-id="directory.id" :subdirectories="directories"/>
        </div>
        <div v-else class="text-gray-500">
          Loading directories...
        </div>
        
      </div>
      <div class="flex-col w-full md:w-1/2 rounded-lg border-2 p-4">
        
        <div v-if="documents && directory" class="h-full">
          <DocumentList :documents="documents" :parent-id="directory.id"/>
        </div>
        <div v-else class="text-gray-500">
          Loading documents...
        </div>
        
      </div>
    </div>
  </div>
</template>

<style scoped>
</style>
