const loadPortfolioBtn = document.getElementById("loadPortfolioBtn");
const investorIdInput = document.getElementById("investorIdInput");
const portfolioMessage = document.getElementById("portfolioMessage");
const portfolioDisplay = document.getElementById("portfolioDisplay");

loadPortfolioBtn.addEventListener("click", async () => {

    // Clear previous state
    portfolioMessage.textContent = "";
    portfolioDisplay.classList.add("d-none");

    const id = investorIdInput.value;

    if (!id){
        portfolioMessage.textContent = "Please enter an investor ID";
        return;
    }

    try {
        const response = await fetch("/investors/" + id + "/portfolio");

        if (!response.ok){
            const error = await response.json();
            portfolioMessage.textContent = error.error || ("HTTP " + response.status);
            return;
        }

        const data = await response.json();
        renderPortfolio(data);

    } catch (err) {
        portfolioMessage.textContent = "Network error: " + err.message;
    }
});


function renderPortfolio(data){
    // Fill investor details
    document.getElementById("investorName").textContent = data.investor.fullName;
    document.getElementById("investorEmail").textContent = data.investor.email;
    document.getElementById("investorAge").textContent = data.investor.age;

    // Fill products table
    const tbody = document.getElementById("productsTableBody");
    tbody.innerHTML = "";  // clear old rows

    for (const product of data.products){
        const row = document.createElement("tr");
        row.innerHTML =
            "<td>" + product.id + "</td>" +
            "<td>" + product.type + "</td>" +
            "<td>R " + product.balance.toFixed(2) + "</td>";
        tbody.appendChild(row);
    }

    // Show the display section
    portfolioDisplay.classList.remove("d-none");
}

const submitWithdrawalBtn = document.getElementById("submitWithdrawalBtn");
const withdrawProductIdInput = document.getElementById("withdrawProductIdInput");
const withdrawAmountInput = document.getElementById("withdrawAmountInput");
const withdrawalMessage = document.getElementById("withdrawalMessage");

submitWithdrawalBtn.addEventListener("click", async () => {

    // Clear previous message
    withdrawalMessage.textContent = "";
    withdrawalMessage.className = "";

    const productId = withdrawProductIdInput.value;
    const amount = withdrawAmountInput.value;

    // Frontend validation (UI validation - one of the advanced requirements)
    if (!productId || !amount){
        showWithdrawalError("Please fill in both Product ID and Amount");
        return;
    }
    if (Number(amount) <= 0){
        showWithdrawalError("Amount must be greater than 0");
        return;
    }

    try {
        const response = await fetch("/withdrawals", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                productId: Number(productId),
                amount: Number(amount)
            })
        });

        if (!response.ok){
            const error = await response.json();
            showWithdrawalError(error.error || ("HTTP " + response.status));
            return;
        }

        const notice = await response.json();
        showWithdrawalSuccess("Withdrawal of R " + notice.amount.toFixed(2) + " successful. Notice ID: " + notice.id);

        // Refresh the portfolio if it's currently shown
        if (!portfolioDisplay.classList.contains("d-none")){
            loadPortfolioBtn.click();
        }

    } catch (err) {
        showWithdrawalError("Network error: " + err.message);
    }
});

function showWithdrawalSuccess(msg){
    withdrawalMessage.textContent = msg;
    withdrawalMessage.className = "alert alert-success";
}

function showWithdrawalError(msg){
    withdrawalMessage.textContent = msg;
    withdrawalMessage.className = "alert alert-danger";
}

// === History ===
const loadHistoryBtn = document.getElementById("loadHistoryBtn");
const historyInvestorIdInput = document.getElementById("historyInvestorIdInput");
const historyMessage = document.getElementById("historyMessage");
const historyTableBody = document.getElementById("historyTableBody");

loadHistoryBtn.addEventListener("click", async () => {

    historyMessage.textContent = "";
    historyTableBody.innerHTML = "";

    const id = historyInvestorIdInput.value;
    if (!id){
        historyMessage.textContent = "Please enter an investor ID";
        return;
    }

    try {
        const response = await fetch("/withdrawals/investor/" + id);

        if (!response.ok){
            const error = await response.json();
            historyMessage.textContent = error.error || ("HTTP " + response.status);
            return;
        }

        const notices = await response.json();

        if (notices.length === 0){
            historyMessage.textContent = "No withdrawals found for this investor";
            return;
        }

        for (const notice of notices){
            const row = document.createElement("tr");
            row.innerHTML =
                "<td>" + notice.id + "</td>" +
                "<td>" + notice.date + "</td>" +
                "<td>" + notice.product.type + "</td>" +
                "<td>R " + notice.amount.toFixed(2) + "</td>";
            historyTableBody.appendChild(row);
        }

    } catch (err) {
        historyMessage.textContent = "Network error: " + err.message;
    }
});


// === CSV download ===
const downloadCsvBtn = document.getElementById("downloadCsvBtn");
const csvFromInput = document.getElementById("csvFromInput");
const csvToInput = document.getElementById("csvToInput");
const csvMessage = document.getElementById("csvMessage");

downloadCsvBtn.addEventListener("click", () => {

    csvMessage.textContent = "";

    const id = historyInvestorIdInput.value;
    const from = csvFromInput.value;
    const to = csvToInput.value;

    if (!id){
        csvMessage.textContent = "Please enter an investor ID above";
        return;
    }
    if (!from || !to){
        csvMessage.textContent = "Please pick both From and To dates";
        return;
    }
    if (from > to){
        csvMessage.textContent = "From date must be before To date";
        return;
    }

    const url = "/withdrawals/investor/" + id + "/export?from=" + from + "&to=" + to;

    // Triggers a file download because the backend sets Content-Disposition: attachment
    window.location.href = url;
});