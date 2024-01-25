import React from 'react';

let size = 0;
let dp = 0;
let forms = {};

export default function FormService(fields=[], data={}, liveValidation=false){
	const [formData, setFormData] = React.useState({error:{'__hasError':false}, data:{}}); 

	const init = ()=>{
        let index = 0;
		size = fields.length;
		dp =  Math.pow(2, size)-1;
        for(let field of fields){
            const id = field.id, value = data[id];
            forms[id] = {...field, index: index++};
            validate(id, value);
            formData.data[id] = value; 
        }
        if(liveValidation) setFormData({...formData});
	}

	const format = (id, value)=>{
		try{
			if(!forms[id]) return;
			if(forms[id].format) return forms[id].format(value);
		}catch(err){
			console.log(err);
		}
		return value;
	}

	const onChange = (id, value)=>{
        validate(id, value);
        formData.data[id] = format(id, value);
        if(liveValidation) setFormData({...formData});
	}

	const validate = (id, value)=>{
		let error = "";
		if(forms[id]){
			setValid(id, true);
			if(!value){
				if(forms[id].is_required){
					setValid(id, false);
					error = `Field is required`;
				}
			}
			else{
				if(forms[id].validation){
					try{
						forms[id].validation(value);	
					}catch(err){
						setValid(id, false);
						error = err.message;
					}	
				}
			}
		}
		formData.error[id] = error;
		formData.error['__hasError'] = hasError();
	}

	const setValid = (id, val)=>{
		val = val?1:0;
		const index = forms[id].index;
		dp = ((~(1<<index)) & dp) | (val<<index);
	}

	const hasError = ()=>{
		return (dp + 1) != Math.pow(2, size);
	}

    const submitData = ()=>{
        setFormData({...formData});
        if(!hasError()) return formData.data;
		return null;
    }

	React.useEffect(()=>{
		init(fields, data);
	}, []);

	return {data: formData.data, error: formData.error, onChange, submitData};
}
