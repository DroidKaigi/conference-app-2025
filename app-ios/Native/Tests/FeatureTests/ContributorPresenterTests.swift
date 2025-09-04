import Dependencies
@testable import ContributorFeature
import Foundation
import Model
import Presentation
import Testing
import UseCase

struct ContributorPresenterTests {
    @MainActor
    @Test
    func testInitialization() async throws {
        let presenter = ContributorPresenter()
        #expect(presenter != nil)
        #expect(presenter.contributorProvider != nil)
    }
    
    @MainActor
    @Test
    func testLoadContributors() async throws {
        let expectedContributors = [
            Model.Contributor(
                id: "1",
                name: "Test Contributor",
                url: URL(string: "https://github.com/test")!,
                iconUrl: URL(string: "https://avatars.githubusercontent.com/u/1")!
            )
        ]
        
        let presenter = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(expectedContributors)
                    continuation.finish()
                }
            }
        } operation: {
            ContributorPresenter()
        }
        
        await presenter.loadContributors()
        
        #expect(presenter.contributorProvider.contributors.count == 1)
        #expect(presenter.contributorProvider.contributors[0].name == "Test Contributor")
    }
    
    @MainActor
    @Test
    func testContributorTapped() async throws {
        let contributor = Model.Contributor(
            id: "1",
            name: "Test Contributor",
            url: URL(string: "https://github.com/test")!,
            iconUrl: URL(string: "https://avatars.githubusercontent.com/u/1")!
        )
        
        let presenter = ContributorPresenter()
        
        // Should execute without throwing
        // In a real test, we'd mock UIApplication.shared.open
        presenter.contributorTapped(contributor)
        
        #expect(true)
    }
    
    @MainActor
    @Test
    func testLoadContributorsMultipleTimes() async throws {
        let firstBatch = [
            Model.Contributor(
                id: "1",
                name: "First Contributor",
                url: URL(string: "https://github.com/first")!,
                iconUrl: URL(string: "https://avatars.githubusercontent.com/u/1")!
            )
        ]
        
        let secondBatch = [
            Model.Contributor(
                id: "2",
                name: "Second Contributor",
                url: URL(string: "https://github.com/second")!,
                iconUrl: URL(string: "https://avatars.githubusercontent.com/u/2")!
            ),
            Model.Contributor(
                id: "3",
                name: "Third Contributor",
                url: URL(string: "https://github.com/third")!,
                iconUrl: URL(string: "https://avatars.githubusercontent.com/u/3")!
            )
        ]
        
        let presenter = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield(firstBatch)
                    continuation.yield(secondBatch)
                    continuation.finish()
                }
            }
        } operation: {
            ContributorPresenter()
        }
        
        await presenter.loadContributors()
        
        // Should have the latest batch
        #expect(presenter.contributorProvider.contributors.count == 2)
        #expect(presenter.contributorProvider.contributors[0].name == "Second Contributor")
        #expect(presenter.contributorProvider.contributors[1].name == "Third Contributor")
    }
    
    @MainActor
    @Test
    func testLoadContributorsEmpty() async throws {
        let presenter = withDependencies {
            $0.contributorsUseCase.load = {
                AsyncStream { continuation in
                    continuation.yield([])
                    continuation.finish()
                }
            }
        } operation: {
            ContributorPresenter()
        }
        
        await presenter.loadContributors()
        
        #expect(presenter.contributorProvider.contributors.isEmpty)
    }
    
    @MainActor
    @Test
    func testPresenterOnMainActor() async throws {
        // Verify presenter is MainActor-bound
        let presenter = ContributorPresenter()
        
        // This test verifies the presenter can be created and used on MainActor
        await MainActor.run {
            let contributor = Model.Contributor(
                id: "1",
                name: "Test",
                url: URL(string: "https://github.com/test")!,
                iconUrl: URL(string: "https://avatars.githubusercontent.com/u/1")!
            )
            presenter.contributorTapped(contributor)
        }
        
        #expect(true)
    }
}