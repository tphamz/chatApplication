export const fields = [
    {
        id: "username",
        is_required: true, 
        validation: (val)=>{
        if(!(/^\w+$/).test(val))
            throw new Error("Invalid character");
    }
}
];