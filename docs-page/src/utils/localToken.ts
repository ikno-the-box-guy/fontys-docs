import {useStorage} from "@vueuse/core";

const token = useStorage<string>(
    'token', 
    null, 
    localStorage,
    {
        mergeDefaults: true,
    }
)

export default token;