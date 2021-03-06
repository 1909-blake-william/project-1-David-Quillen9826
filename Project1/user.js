let currentUser;
function newReimbursementSubmit(event) {
    event.preventDefault(); // stop page from refreshing
    console.log('submitted');

    const reimbursement = getReimbursementFromInputs();
    //console.log(reimbursement)

    fetch('http://localhost:5500/project1/reimbursements', {
        method: 'POST',
        body: JSON.stringify(reimbursement),
        headers: {
            'content-type': 'application/json'
        }
    })
        .then(res => res.json())
        .then(data => {
            addReimbursementToTable(data);
            //console.log(data);
        })
        .catch(err => console.log(err));


}

function addReimbursementToTable(reimbursement) {

    const row = document.createElement('tr');

    const amountData = document.createElement('td');
    amountData.innerText = reimbursement.reimbAmount;
    row.appendChild(amountData);

    const timeSubData = document.createElement('td');
    timeSubData.innerText = new Date(reimbursement.reimbSubmitted);
    row.appendChild(timeSubData);

    const timeResData = document.createElement('td');
    if (!reimbursement.reimbResolved) {
        timeResData.innerText = 'Not Yet Resolved.'
    } else {
        timeResData.innerText = new Date(reimbursement.reimbResolved);
    }
    row.appendChild(timeResData);

    const reimbDesData = document.createElement('td');
    reimbDesData.innerText = reimbursement.reimbDescription;
    row.appendChild(reimbDesData);
    
    const reimbResolverData = document.createElement('td');
    reimbResolverData.innerText = reimbursement.reimbResName;
    row.appendChild(reimbResolverData);

    const reimbStatData = document.createElement('td');
    reimbStatData.innerText = reimbursement.reimbStatus;
    row.appendChild(reimbStatData);

    const reimbTypeData = document.createElement('td');
    reimbTypeData.innerText = reimbursement.reimbType;
    row.appendChild(reimbTypeData);

    document.getElementById('reimbursement-table-body').appendChild(row);
}

function getReimbursementFromInputs() {
    const amount = +document.getElementById('reimbursement-amount-input').value;
    //console.log(amount)
    const description = document.getElementById('reimbursement-description-input').value;
    //console.log(description)
    const type = document.getElementById('reimbursement-type-select').value;
    let typeId;
    if (type === 'Lodging') {
        typeId = 1
    } else if (type === 'Travel') {
        typeId = 2
    } else if (type === 'Food') {
        typeId = 3
    } else {
        typeId = 4
    }
    //console.log(typeId)
    const author = currentUser.ersUsersId
    console.log(author)

    const reimbursement = {
        reimbAmount: amount,
        reimbDescription: description,
        reimbTypeId: typeId,
        reimbAuthor: author
    }
    //console.log(reimbursement)
    return reimbursement;
}

function refreshTable() {
    fetch(`http://localhost:5500/project1/reimbursements?user=${currentUser.ersUsersId}`, { //
        credentials: 'include'
    })
        .then(res => res.json())
        .then(data => {
            let numRows = document.getElementById('reimbursement-table-body').rows.length
            for (let i = numRows - 1; i > -1; i--) {
                document.getElementById('reimbursement-table-body').deleteRow(i)
            }
            data.forEach(addReimbursementToTable)
        })
        .catch(console.log);
}

function getCurrentUserInfo() {
    fetch('http://localhost:5500/project1/auth/session-user', {
        credentials: 'include'
    })
        .then(resp => resp.json())
        .then(data => {
            document.getElementById('users-name').innerText = data.ersUsername
            //logout causes data to be null so it can't read username of it
            currentUser = data;
            // if (currentUser.userRoleId === 2) {
            //     window.location = '/user-manager.html'
            // }
            //console.log(currentUser)
            refreshTable();

        })
        .catch(err => {
            console.log(err)
            window.location = '/login.html'
        })
}

function logout(event) {
    //event.preventDefault();
    console.log('logout function')
    fetch('http://localhost:5500/project1/auth/logout', {
        method: 'POST',
        headers: {
            'content-type': 'application/json'
        },
        mode: 'cors',
        credentials: 'include', // put credentials: 'include' on every request to use session info
        //body: JSON.stringify(credential)
    })
        .then(resp => {
            // if(resp.status === 201) {
            //     // redirect
            getCurrentUserInfo();
            //window.location = '/login.html';
            // } else {
            //     //document.getElementById('error-message').innerText = 'Failed to login';
            // }
        })
}

getCurrentUserInfo();
