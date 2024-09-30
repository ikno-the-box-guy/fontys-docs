import {useStorage} from "@vueuse/core";

interface LocalUser {
    name: string;
    email: string;
    root: number;
}

const user = useStorage<LocalUser>(
    'user',
    null,
    localStorage,
    {
        mergeDefaults: true,
        serializer: {
            read: (v: any) => v ? JSON.parse(v) : null,
            write: (v: any) => JSON.stringify(v),
        },
    }
)

export default user;