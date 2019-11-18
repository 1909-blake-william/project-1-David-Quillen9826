function login(event) {
    event.preventDefault();
    console.log("hit button")
    const Username = document.getElementById('inputUsername').value;
    const Password = document.getElementById('inputPassword').value;
    const credential = {
        Username,
        Password
    };
    console.log(credential)

    fetch('http://localhost:5500/project1/auth/login', {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        },
        mode: 'cors',
        credentials: 'include',
        body: JSON.stringify(credential)
    })
        .then(resp => {
            if (resp.status === 201) {
                // redirect
                window.location = '/user.html';
            } else {
                document.getElementById('error-message').innerText = 'Failed to login';
            }
        })


}