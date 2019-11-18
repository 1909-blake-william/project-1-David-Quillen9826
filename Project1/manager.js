

let currentUser;

function addReimbursementToTable(reimbursement) {

    // create the row element
    const row = document.createElement('tr');

    // create all the td elements and append them to the row
    const idData = document.createElement('td');
    idData.innerText = reimbursement.reimbId;
    row.appendChild(idData);

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

    const reimbAuthData = document.createElement('td');
    reimbAuthData.innerText = reimbursement.reimbName;
    row.appendChild(reimbAuthData);

    const reimbAuthIdData = document.createElement('td');
    reimbAuthIdData.innerText = reimbursement.reimbAuthor;
    row.appendChild(reimbAuthIdData);

    const reimbResolverData = document.createElement('td');
    reimbResolverData.innerText = reimbursement.reimbResName;
    row.appendChild(reimbResolverData);

    const reimbStatData = document.createElement('td');
    reimbStatData.innerText = reimbursement.reimbStatus;
    row.appendChild(reimbStatData);

    const reimbTypeData = document.createElement('td');
    reimbTypeData.innerText = reimbursement.reimbType;
    row.appendChild(reimbTypeData);

    if (reimbursement.reimbStatus === 'Pending') {
        const reimbYesButton = document.createElement('button');
        reimbYesButton.innerText = 'Approve';
        reimbYesButton.setAttribute('onclick', 'approve(this.value)')
        reimbYesButton.setAttribute('class', 'btn btn-lg btn-success btn-block')
        reimbYesButton.setAttribute('value', `${reimbursement.reimbId}`)
        row.appendChild(reimbYesButton);

        const reimbNoButton = document.createElement('button');
        reimbNoButton.innerText = 'Deny';
        reimbNoButton.setAttribute('onclick', 'deny(this.value)')
        reimbNoButton.setAttribute('class', 'btn btn-lg btn-danger btn-block')
        reimbNoButton.setAttribute('value', `${reimbursement.reimbId}`)
        row.appendChild(reimbNoButton);
    }

    // append the row into the table
    document.getElementById('reimbursement-table-body').appendChild(row);
}

function getCurrentUserInfo() {
    fetch('http://localhost:5500/project1/auth/session-user', {
        credentials: 'include'
    })
        .then(resp => resp.json())
        .then(data => {
            document.getElementById('users-name').innerText = data.ersUsername
            currentUser = data;
            if (currentUser.userRoleId === 1) {
                window.location = '/user.html'
            }
            refreshTable();

        })
        .catch(err => {
            console.log(err)
            window.location = '/login.html'
        })
}

function approve(id) {
    event.preventDefault(); // stop page from refreshing
    console.log('updated');

    fetch(`http://localhost:5500/project1/reimbursements?status=2&resolver=${currentUser.ersUsersId}&id=${id}`, {
        method: 'PUT',

        headers: {
            'content-type': 'application/json'
        }
    })
    .then(resp => {
        if (resp.status === 204) {
            refreshTable()
        } else {
            console.log('approve went wrong')
        }
    })

        .catch(err => console.log(err));

}

function deny(id) {
    event.preventDefault(); // stop page from refreshing
    console.log('updated');

    fetch(`http://localhost:5500/project1/reimbursements?status=3&resolver=${currentUser.ersUsersId}&id=${id}`, {
        method: 'PUT',

        headers: {
            'content-type': 'application/json'
        }
    })
    .then(resp => {
        if (resp.status === 204) {
            refreshTable()
        } else {
            console.log('deny went wrong')
        }
    })

        .catch(err => console.log(err));

}

getCurrentUserInfo();
