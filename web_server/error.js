module.exports = function(message, code){
    let error = new Error(message);
    error.statusCode = code || 500;
    throw error;
  }