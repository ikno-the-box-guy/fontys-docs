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
const hierarchy = ref<Directory[]>();
const directories = ref<Directory[]>();
const documents = ref<Document[]>();

const fetchDirectoryHierarchy = async () => {
  documentApi.get("/directories/" + route.params.directory + "/hierarchy")
      .then((response) => {
        hierarchy.value = response.data as Directory[];
        directory.value = hierarchy.value[0];
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
  fetchDirectoryHierarchy();
  fetchSubdirectories();
  fetchDocuments();
});

watch(route, () => {
  fetchDirectoryHierarchy();
  fetchSubdirectories();
  fetchDocuments();
});
</script>

<template>
  <div class="p-6 min-h-full xl:px-32">
    <div v-if="directory && directory.id !== directory.parentId" class="mb-6 text-3xl flex ml-6">
      <template v-for="dir in hierarchy.slice().reverse()">
        <RouterLink :to="'/explorer/' + dir.id" class="nav-link" v-if="dir.id !== directory.id">
          {{ dir.id !== dir.parentId ? dir.displayName : '~' }}
        </RouterLink>
        <span class="text-gray-400" v-if="dir.id !== hierarchy[0].id">/</span>
      </template>

      <span class="font-semibold text-gray-800">{{ directory.displayName }}</span>
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
.nav-link {
  @apply text-gray-600 hover:text-blue-600 hover:underline;
}
</style>
