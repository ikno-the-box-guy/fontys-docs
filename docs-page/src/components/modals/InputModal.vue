<script setup lang="ts">
import {VueFinalModal} from 'vue-final-modal'
import {ref} from "vue";

defineProps<{
  title: string;
  description: string;
  buttons: Button[];
  showInput: boolean;
  inputPlaceholder: string;
}>()

export interface Button {
  label: string;
  action: string;
}

// Emits
const emit = defineEmits<{
  (e: 'button-click', payload: { action: string; inputValue: string }): void;
  (e: 'close'): void;
}>();

// Reactive input value
const inputValue = ref<string>('');

// Methods
const handleButtonClick = (action: string) => {
  emit('button-click', {action, inputValue: inputValue.value});
};
</script>

<template>
  <VueFinalModal
      class="flex justify-center items-center"
      content-class="flex flex-col max-w-xl mx-4 p-4 bg-white dark:bg-gray-900 border dark:border-gray-700 rounded-lg space-y-2"
  >
    <h3 class="text-lg font-semibold mb-2">{{ title }}</h3>
    <p class="text-sm text-gray-700 mb-4">{{ description }}</p>

    <input
        v-if="showInput"
        v-model="inputValue"
        :placeholder="inputPlaceholder"
        class="input-field border p-2 w-full rounded mb-4"
        type="text"
    />

    <div class="buttons flex justify-end space-x-2">
      <button
          v-for="(button, index) in buttons"
          :key="index"
          @click="handleButtonClick(button.action)"
          class="popup-button px-4 py-2 rounded bg-blue-500 text-white hover:bg-blue-600"
      >
        {{ button.label }}
      </button>
    </div>
  </VueFinalModal>
</template>